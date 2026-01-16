terraform {
  required_version = ">= 1.0"
  backend "s3" {
    bucket = "tech-challenge-hackathon"
    key    = "auth-service/terraform.tfstate"
    region = "us-east-1"
  }
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

# --- Data Sources (Busca recursos existentes) ---

data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "all" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

data "aws_iam_role" "lab_role" {
  name = "LabRole"
}

# --- ECR (Repositório de Imagem) ---
# Imagem é privada/local
resource "aws_ecr_repository" "auth_service" {
  name                 = "auth-service-repo"
  image_tag_mutability = "MUTABLE"
  force_delete         = true

  image_scanning_configuration {
    scan_on_push = true
  }
}

# --- Security Groups ---

resource "aws_security_group" "alb_sg" {
  name        = "auth-service-alb-sg"
  description = "Security group for ALB - Allow HTTP"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "Allow HTTP from world"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "auth-service-alb-sg"
  }
}

resource "aws_security_group" "ecs_sg" {
  name        = "auth-service-ecs-sg"
  description = "Security group for ECS tasks"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "auth-service-ecs-sg"
  }
}

resource "aws_security_group" "rds_sg" {
  name        = "auth-rds-sg"
  description = "Security group for RDS MySQL"
  vpc_id      = data.aws_vpc.default.id

  # Permite acesso da aplicação ECS
  ingress {
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.ecs_sg.id]
  }

  # Permite acesso externo (para seu Workbench funcionar)
  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "auth-rds-sg"
  }
}

# --- Database (RDS MySQL) ---

resource "aws_db_subnet_group" "auth_db_subnet" {
  name       = "auth-db-subnet-group"
  subnet_ids = slice(data.aws_subnets.all.ids, 0, min(2, length(data.aws_subnets.all.ids)))

  tags = {
    Name = "auth-db-subnet-group"
  }
}

resource "aws_db_instance" "auth_db" {
  identifier              = "auth-service-db"
  instance_class          = "db.t3.micro"
  allocated_storage       = 20
  engine                  = "mysql"
  engine_version          = "8.0"
  username                = "root"
  password                = "12345678"
  db_name                 = "auth_db"
  parameter_group_name    = "default.mysql8.0"
  skip_final_snapshot     = true
  publicly_accessible     = true
  vpc_security_group_ids  = [aws_security_group.rds_sg.id]
  db_subnet_group_name    = aws_db_subnet_group.auth_db_subnet.name

  apply_immediately       = true
  backup_retention_period = 0
  deletion_protection     = false

  tags = {
    Name = "auth-service-db"
  }
}

# --- Load Balancer (ALB) ---

resource "aws_lb" "auth_alb" {
  name                       = "auth-service-alb"
  internal                   = false
  load_balancer_type         = "application"
  security_groups            = [aws_security_group.alb_sg.id]
  subnets                    = slice(data.aws_subnets.all.ids, 0, min(2, length(data.aws_subnets.all.ids)))
  enable_deletion_protection = false

  tags = {
    Name = "auth-service-alb"
  }
}

resource "aws_lb_target_group" "auth_tg" {
  name        = "auth-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.default.id
  target_type = "ip"

  health_check {
    path                = "/actuator/health" # O endpoint do Spring Actuator
    interval            = 60
    timeout             = 30
    healthy_threshold   = 2
    unhealthy_threshold = 5
    matcher             = "200"
  }

  tags = {
    Name = "auth-tg"
  }
}

resource "aws_lb_listener" "auth_listener" {
  load_balancer_arn = aws_lb.auth_alb.arn
  port              = "80"
  protocol          = "HTTP"

default_action {
    type = "fixed-response"

    fixed_response {
      content_type = "text/plain"
      message_body = "Acesso Direto Negado."
      status_code  = "403"
    }
  }
}

resource "aws_lb_listener_rule" "allow_gateway" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 100

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.main.arn
  }

  condition {
    http_header {
      http_header_name = "x-apigateway-token"
      values           = ["tech-challenge-hackathon"]
    }
  }
}

# --- ECS Cluster & Task ---

resource "aws_ecs_cluster" "auth_cluster" {
  name = "auth-cluster"

  setting {
    name  = "containerInsights"
    value = "disabled"
  }

  tags = {
    Name = "auth-cluster"
  }
}

resource "aws_ecs_task_definition" "auth_task" {
  family                   = "auth-service-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 512
  memory                   = 1024
  execution_role_arn       = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([{
    name  = "auth-service"
    image = "${aws_ecr_repository.auth_service.repository_url}:latest"
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
      protocol      = "tcp"
    }]
    essential = true

    logConfiguration = {
      logDriver = "awslogs"
      options = {
        awslogs-group         = "/ecs/auth-service"
        awslogs-region        = "us-east-1"
        awslogs-stream-prefix = "ecs"
        awslogs-create-group  = "true"
      }
    }

    environment = [
      {
        name  = "SPRING_DATASOURCE_URL"
        value = "jdbc:mysql://${aws_db_instance.auth_db.endpoint}/auth_db?createDatabaseIfNotExist=true"
      },
      {
        name  = "SPRING_DATASOURCE_USERNAME"
        value = "root"
      },
      {
        name  = "SPRING_DATASOURCE_PASSWORD"
        value = "12345678"
      },
      {
        name  = "JWT_SECRET"
        value = "minha-chave-secreta-do-lab"
      },
      {
        name  = "SERVER_PORT"
        value = "8080"
      }
    ]
  }])

  tags = {
    Name = "auth-service-task"
  }
}

resource "aws_ecs_service" "auth_service" {
  name            = "auth-service"
  cluster         = aws_ecs_cluster.auth_cluster.id
  task_definition = aws_ecs_task_definition.auth_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  health_check_grace_period_seconds = 300

  network_configuration {
    security_groups  = [aws_security_group.ecs_sg.id]
    subnets          = slice(data.aws_subnets.all.ids, 0, min(2, length(data.aws_subnets.all.ids)))
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.auth_tg.arn
    container_name   = "auth-service"
    container_port   = 8080
  }


  deployment_controller {
    type = "ECS"
  }

  tags = {
    Name = "auth-service"
  }

  depends_on = [aws_lb_listener.auth_listener]
}

# --- Outputs (Para facilitar sua vida) ---

output "ecr_repo" {
  value = aws_ecr_repository.auth_service.repository_url
}

output "rds_endpoint" {
  value = aws_db_instance.auth_db.endpoint
}

output "api_url" {
  value = "http://${aws_lb.auth_alb.dns_name}"
}
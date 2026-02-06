output "ecr_repo" {
  value = aws_ecr_repository.auth_service.repository_url
}
output "rds_endpoint" {
  value = aws_db_instance.auth_db.endpoint
}
output "api_url" {
  value = "http://${aws_lb.auth_alb.dns_name}"
}
output "ssm_auth_alb_dns_path" {
  value = aws_ssm_parameter.auth_alb_dns.name
}

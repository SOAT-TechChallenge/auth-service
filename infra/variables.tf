variable "db_password" {
  description = "Senha do banco de dados RDS"
  type        = string
  sensitive   = true
}

variable "jwt_secret" {
  description = "Secret para assinatura do JWT"
  type        = string
  sensitive   = true
}
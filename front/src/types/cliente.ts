// con clienteADminDto
export interface ClienteAdminDTO {
  id: number;
  email: string;
  createdAt: string;
  updatedAt: string;
  nombre: string;
  apellido: string;
  dni: string;
  tieneTurnosActivos: boolean;
  clienteActivo: boolean;
}

// listas activos y no activos
export interface ClientesAdminResponse {
  clientesActivos: ClienteAdminDTO[];
  clientesBajaLogica: ClienteAdminDTO[];
}
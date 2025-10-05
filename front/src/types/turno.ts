// DTOs para Servicios
export interface ServicioResponseDTO {
  id: number;
  nombre: string;
  descripcion?: string;
}

// DTOs para Ubicaciones
export interface UbicacionResponseDTO {
  id: number;
  nombre: string;
  direccion?: string;
}

// DTO para Crear Turno
export interface TurnoRequestDTO {
  idCliente?: number;
  idServicio: number;
  idUbicacion: number;
  fecha: string;
  hora: string;
}


export interface TurnoResponseDTO {
  id: number;
  fecha: string;
  hora: string;
  nombreServicio: string | null;
  ubicacionDescripcion: string;
  nombreEmpleado: string | null;
  nombreCliente: string | null;
  codigoAnulacion: string;
}

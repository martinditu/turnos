// Representa un horario individual disponible para un turno
export interface Horario {
  horaInicio: string; // Ej: "09:00"
  horaFin: string;    // Ej: "09:30"
}

// Respuesta del backend para una fecha especifica de disponibilidad
export interface DisponibilidadResponse {
  fecha: string;
  activo: boolean;
  horarios: Horario[];
}

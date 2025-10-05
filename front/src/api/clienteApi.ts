

// este servicio maneja las llamadas HTTP del cliente autenticado al back
// utiliza una instancia de Axios con token incluido automaticamente

import axios, { AxiosInstance, AxiosResponse } from "axios";
import {
  ServicioResponseDTO,
  UbicacionResponseDTO,
  TurnoRequestDTO,
  TurnoResponseDTO,
} from "../types/Turno";

// dto para preview de turno antes de confirmars
export interface TurnoPreviewDTO {
  idServicio: number;
  idUbicacion: number;
  fecha: string;
  hora: string;
}

// url base para endpoint cliente
const API_BASE_CLIENTE = "http://localhost:8080/api/cliente";

// instancia de axios para cliente autenticado
const axiosInstanceCliente: AxiosInstance = axios.create({
  baseURL: API_BASE_CLIENTE,
  timeout: 5000,
});

// agrega token jwt al header Auth en cada request
axiosInstanceCliente.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers = config.headers || {};
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// si request falla con 401 redirect login
axiosInstanceCliente.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

// para extraer datos del response y manejar errores
async function extraerDatos<T>(promise: Promise<AxiosResponse<T>>): Promise<T> {
  try {
    const response = await promise;
    return response.data;
  } catch (error) {
    throw error;
  }
}

// METODOS PARA CLIENTE AUTH

// anular un turno con codigo de anulacion
export const anularTurno = (codigoAnulacion: string): Promise<string> =>
  extraerDatos(
    axiosInstanceCliente.post("/anular", null, { params: { codigoAnulacion } })
  ).then((data) => data.mensaje);

// confirma los datos de un turno seleccionado
export const confirmarDatosTurno = (
  turno: TurnoPreviewDTO
): Promise<TurnoResponseDTO> =>
  extraerDatos(axiosInstanceCliente.post("/confirmar", turno));


// trae los turnos del usuario actual
export const traerMisTurnos = (): Promise<TurnoResponseDTO[]> =>
  extraerDatos(axiosInstanceCliente.get("/mis-turnos"));

// traer los servicios disponibles
export const traerServicios = (): Promise<ServicioResponseDTO[]> =>
  extraerDatos(axiosInstanceCliente.get("/servicios"));

// traer las ubi para un servicio
export const traerUbicaciones = (servicioId: number): Promise<UbicacionResponseDTO[]> =>
  extraerDatos(axiosInstanceCliente.get(`/servicios/${servicioId}/ubicaciones`));

// traer los dias disp para un servicio
export const traerDiasDisponibles = (servicioId: number): Promise<string[]> =>
  extraerDatos(axiosInstanceCliente.get(`/servicios/${servicioId}/dias-disponibles`));

// traer los horarios disp para un servicio en una fecha especifica
export const traerHorariosDisponibles = (
  servicioId: number,
  fecha: string
): Promise<string[]> =>
  extraerDatos(
    axiosInstanceCliente.get(`/servicios/${servicioId}/fechas/${fecha}/horarios`)
  );

// trare las fechas habilitadas par aun servicio en un mes y año especifico
export const traerFechasHabilitadas = (
  servicioId: number,
  year: number,
  month: number
): Promise<string[]> =>
  extraerDatos(
    axiosInstanceCliente.get(`/servicios/${servicioId}/fechas-habilitadas`, {
      params: { year, month },
    })
  );

// crear nuevo turno
export const crearTurno = (turno: TurnoRequestDTO): Promise<TurnoResponseDTO> =>
  extraerDatos(axiosInstanceCliente.post("", turno));

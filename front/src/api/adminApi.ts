// src/api/AdminService.ts

// Este servicio administra las peticiones al back que solo puede hacer un admin
// Permite gestionar clientes y turnos desde el panel admin

import axios, { AxiosInstance } from "axios";
import { ClienteAdminDTO, ClientesAdminResponse } from "../types/Cliente";
import { TurnoResponseDTO } from "../types/Turno";

// url base para endpoint admin
const API_BASE_ADMIN = "http://localhost:8080/api/admin";

// instancia de Axios
const axiosInstance: AxiosInstance = axios.create({
  baseURL: API_BASE_ADMIN,
  timeout: 5000,
});

// interceptor para token JWT
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token && config.headers) {
      config.headers.set?.("Authorization", `Bearer ${token}`);
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// FUNCIONES DEL ADMIN

// traer todos los clientes (activos / inactivos)
export const traerClientes = async (): Promise<ClientesAdminResponse> => {
  const response = await axiosInstance.get("/clientes");
  return response.data;
};

// soft delete cliente por ID
export const darDeBajaCliente = async (id: number): Promise<void> => {
  await axiosInstance.put(`/${id}/baja`);
};

// reactivar cliente por ID
export const darDeAltaCliente = async (id: number): Promise<void> => {
  await axiosInstance.put(`/${id}/alta`);
};

// editar cliente
export const editarCliente = async (
  id: number,
  cliente: ClienteAdminDTO
): Promise<ClienteAdminDTO> => {
  const response = await axiosInstance.put(`/${id}`, cliente);
  return response.data;
};

// traer todos los turnos registrados
export const traerTodosLosTurnos = async (): Promise<TurnoResponseDTO[]> => {
  const response = await axiosInstance.get("/turnos");
  return response.data;
};

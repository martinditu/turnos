

// AuthService: se encarga de la comunicacion con el back para auth
// Es la capa que hace peticiones al servidor y maneja seson del lado del cliente

import axios from "axios";
import { Usuario } from "../types/Usuario";

// URL base del back paar la auth
const API_BASE_URL = "http://localhost:8080/api/auth";

// guarda datos del usuario en localStorage
const guardarUsuarioEnLocalStorage = (usuario: Usuario) => {
  localStorage.setItem("token", usuario.token);
  localStorage.setItem("email", usuario.email);
  localStorage.setItem("rol", usuario.rol);
};

// elimina los datos del usuario del localStorage
const limpiarUsuarioDeLocalStorage = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("email");
  localStorage.removeItem("rol");
};

// iniciar sesion y envia mail y pw al back
export const loginUsuario = async (email: string, password: string): Promise<Usuario> => {
  try {
    const { data } = await axios.post(`${API_BASE_URL}/login`, { email, password });

    const { token, email: emailUsuario, rol } = data;

    // valida rta del sv
    if (!token || !emailUsuario || !rol) {
      throw new Error("Respuesta invalida del servidor");
    }

    // limpia el ROLE_ del rol recibido
    const rolLimpio = rol.replace("ROLE_", "") as "ADMIN" | "CLIENTE";

    const usuario: Usuario = { email: emailUsuario, token, rol: rolLimpio };

    // guarda sesion
    guardarUsuarioEnLocalStorage(usuario);

    return usuario;
  } catch (error: any) {
    console.error("Error al iniciar sesion:", error.response?.data || error.message);
    throw new Error("Credenciales incorrectas o error en el servidor.");
  }
};

// cerrar sesion y limpiar localStorage
export const logoutUsuario = () => {
  limpiarUsuarioDeLocalStorage();
};

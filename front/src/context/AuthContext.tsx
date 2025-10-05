
// define un "contexto global" de autenticacion.
// Sirve para que cualquier componente pueda acceder al usuario logueado, su token y rol.

// createContext: crear un contexto de React
// useContext: se usa para consumir el contexto
// useEffect: ejcuta la logica cuando el componente se monta
// useState: para manejar "user" y "loading"
// User: tipo de TS q representa los datos del usuario
import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";
import { Usuario } from "../types/Usuario"; //dto
import { logoutUsuario } from "../api/authService"; //limpia localStorage al cerrar sesion

// tipado de contexto: define q datos y func estaran disponibles globalmente
type AuthContextType = {
  user: Usuario | null; // usuario logeado o null si no hay
  login: (user: Usuario) => void; // funcion que guarda el usuario en "estado" y "localStorage"
  logout: () => void; // borra sesion
  loading: boolean; // evita mostrar la app antes de verificar si hay sesion activa
};

// creacion del contexto, inicia sin datos. luego sera usado y provisto por "AuthProvider"
const AuthContext = createContext<AuthContextType | undefined>(undefined);

type Props = {
  children: ReactNode;
};

// componente que envuelve la app y maneja estado global de auth
export const AuthProvider = ({ children }: Props) => {
  // carga inicial
  const [user, setUser] = useState<Usuario | null>(null); // guarda usuario en memoria
  const [loading, setLoading] = useState(true); // evita render mientras se busca sesion

  // useEffect: al inicar la app revisa si hay datos sesion guaraddos
  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    const storedEmail = localStorage.getItem("email");
    const storedRol = localStorage.getItem("rol");

    // verifica si hay una sesion guardada
    if (storedToken && storedEmail && storedRol) {
      // si hay , restaura el usuario, limpia ROLE_ADMIN a ADMIN
      const rolLimpio = storedRol.replace("ROLE_", "") as "ADMIN" | "CLIENTE";

      setUser({
        email: storedEmail,
        token: storedToken,
        rol: rolLimpio,
      });
    }

    // loading pasa a false
    setLoading(false);
  }, []);

  // inicia sesion y persistir
  const login = (usuario: Usuario) => {
    const rolLimpio = usuario.rol.replace("ROLE_", "");

    // guarda los datos del usuario en "localStorage"
    localStorage.setItem("token", usuario.token);
    localStorage.setItem("email", usuario.email);
    localStorage.setItem("rol", rolLimpio); // guarda "CLIENTE" o "ADMIN"

    // setUser: actaliza en memoria
    setUser({
      ...usuario,
      rol: rolLimpio as "ADMIN" | "CLIENTE",
    });
  };

  // cerrar sesion, elimina sesion tanto del ¨estado(memoria)¨ como del ¨localStorage¨
  const logout = () => {
    logoutUsuario(); // limpia localStorage desde el servicio
    setUser(null); // limpia el estado
  };

  return (
    // Provider + Hook personalizado
    // esto expone el context a toda la app
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

// useAuthContext(hook perso): puede usarse en cualquier componente para obtener "user","loghin","logout","loading"
export const useAuthContext = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuthContext debe usarse dentro de AuthProvider");
  }
  return context;
};

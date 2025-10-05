import { Navigate } from "react-router-dom";
import { useAuthContext } from "../context/AuthContext";
import { ReactNode } from "react";

type Props = {
  children: ReactNode; // componente o vista a mostrar si el acceso es valido
  rolesPermitidos: ("ADMIN" | "CLIENTE")[]; // lista de roles habilitados
};

// componente para proteger rutas segun autenticacion y rol
const PrivateRoute = ({ children, rolesPermitidos }: Props) => {
  const { user, loading } = useAuthContext();

  if (loading) {
    return <div className="text-center mt-10">Cargando sesion...</div>;
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (!rolesPermitidos.includes(user.rol)) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <>{children}</>;
};

export default PrivateRoute;

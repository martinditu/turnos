/*pattern return: Configuración de rutas y manejo de errores mejorado */
/* Ahora incluye páginas específicas para diferentes tipos de errores */
/* - /unauthorized: Error 403 cuando no tienes permisos */
/* - /server-error: Error 500 cuando falla el backend */
/* - 404: Ruta no encontrada */

import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Login from "./pages/Login";
import Register from "./pages/Register"; // /*pattern return: Página de registro */
import PrivateRoute from "./routes/PrivateRoute";
import DashboardAdminRoutes from "./routes/dashboard/AdminDashboardRoutes";
import DashboardClienteRoutes from "./routes/dashboard/ClienteDashboardRoutes";
import Unauthorized from "./pages/Unauthorized";
import ServerError from "./pages/ServerError";
import NotFound from "./pages/NotFound";

function App() {
  return (
    // AuthProvider permite acceder a la sesion (user, token) desde toda la app
    <AuthProvider>
      {/* habilita el enrutado SPA */}
      <BrowserRouter>
        <Routes>
          {/* ruta publica para login */}
          <Route path="/login" element={<Login />} />
          {/*pattern return: Ruta pública para registro de nuevos clientes */}
          <Route path="/register" element={<Register />} />

          {/*pattern return: Páginas de error personalizadas */}
          <Route path="/unauthorized" element={<Unauthorized />} />
          <Route path="/server-error" element={<ServerError />} />
          <Route path="/not-found" element={<NotFound />} />

          {/* rutas privadas para ADMIN */}
          <Route
            path="/dashboard/admin/*"
            element={
              <PrivateRoute rolesPermitidos={["ADMIN"]}>
                <DashboardAdminRoutes />
              </PrivateRoute>
            }
          />

          {/* rutas privadas para CLIENTE */}
          <Route
            path="/dashboard/cliente/*"
            element={
              <PrivateRoute rolesPermitidos={["CLIENTE"]}>
                <DashboardClienteRoutes />
              </PrivateRoute>
            }
          />

          {/* redirecciones por defecto */}
          <Route path="/" element={<Navigate to="/login" />} />
          {/*pattern return: Rutas no encontradas redirigen a página 404 */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;

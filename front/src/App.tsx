import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Login from "./pages/Login";
import PrivateRoute from "./routes/PrivateRoute";
import DashboardAdminRoutes from "./routes/dashboard/AdminDashboardRoutes";
import DashboardClienteRoutes from "./routes/dashboard/ClienteDashboardRoutes";

function App() {
  return (
    // AuthProvider permite acceder a la sesion (user, token) desde toda la app
    <AuthProvider>
      {/* habilita el enrutado SPA */}
      <BrowserRouter>
        <Routes>
          {/* ruta publica para login */}
          <Route path="/login" element={<Login />} />

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
          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;

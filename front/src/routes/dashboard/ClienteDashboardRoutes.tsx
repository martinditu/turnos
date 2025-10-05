// rutas internas del cliente dentro del dashboard

import { Routes, Route, Navigate } from "react-router-dom";
import DashboardCliente from "../../pages/dashboard/ClienteDashboard";
import MenuCliente from "../../components/menus/MenuCliente";
import ReservaTurno from "../../components/ReservaTurno";
import MisTurnosList from "../../components/MisTurnosList";
import AnularTurno from "../../components/AnularTurno";

const DashboardClienteRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<DashboardCliente />}>
        {/* redirige a menu por defecto */}
        <Route index element={<Navigate to="menu" replace />} />

        {/* opciones de cliente */}
        <Route path="menu" element={<MenuCliente />} />
        <Route path="reservar" element={<ReservaTurno />} />
        <Route path="mis-turnos" element={<MisTurnosList />} />
        <Route path="anular" element={<AnularTurno />} />

        {/* cualquier ruta no valida redirige a menu */}
        <Route path="*" element={<Navigate to="menu" replace />} />
      </Route>
    </Routes>
  );
};

export default DashboardClienteRoutes;

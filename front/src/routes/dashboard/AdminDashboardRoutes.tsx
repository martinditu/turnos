// rutas internas del admin dentro del dashboard

import { Routes, Route, Navigate } from "react-router-dom";
import DashboardAdmin from "../../pages/dashboard/AdminDashboard";
import MenuAdmin from "../../components/menus/MenuAdmin";
import TurnoAdminListWrapper from "../../wrappers/TurnoAdminListWrapper";
import ClienteAdminListWrapper from "../../wrappers/ClienteAdminListWrapper";

const DashboardAdminRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<DashboardAdmin />}>
        {/* redirige a menu por defecto */}
        <Route index element={<Navigate to="menu" replace />} />

        {/* opciones de admin */}
        <Route path="menu" element={<MenuAdmin />} />
        <Route path="turnos" element={<TurnoAdminListWrapper />} />
        <Route path="clientes" element={<ClienteAdminListWrapper />} />

        {/* cualquier ruta no valida redirige a menu */}
        <Route path="*" element={<Navigate to="menu" replace />} />
      </Route>
    </Routes>
  );
};

export default DashboardAdminRoutes;

import { Outlet, useNavigate } from "react-router-dom";
import { useAuthContext } from "../../context/AuthContext";
import useDocumentTitle from "../../hooks/useDocumentTitle";

const DashboardCliente = () => {
  useDocumentTitle("Panel de Cliente");

  const { user, logout } = useAuthContext();
  const navigate = useNavigate();

  const cerrarSesion = () => {
    logout();
    navigate("/login");
  };

  const irAInicio = () => {
    navigate("/dashboard/cliente/menu");
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <header className="flex justify-between items-center mb-6 border-b pb-4">
        <h1 className="text-2xl font-bold">
          Bienvenido, <span className="text-blue-600">{user?.email}</span>
        </h1>
        <div className="space-x-2">
          <button
            onClick={irAInicio}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
          >
            Inicio
          </button>
          <button
            onClick={cerrarSesion}
            className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition"
          >
            Cerrar sesion
          </button>
        </div>
      </header>

      <main>
        <Outlet />
      </main>
    </div>
  );
};

export default DashboardCliente;

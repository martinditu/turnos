/*pattern return: Página de error 403 - Acceso Denegado */
/* Se muestra cuando un usuario intenta acceder a una ruta sin los permisos necesarios */
/* Antes el sistema deslogueaba al usuario, ahora muestra este mensaje amigable */

import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../context/AuthContext";

const Unauthorized = () => {
  const navigate = useNavigate();
  const { user } = useAuthContext();

  /*pattern return: Redirige al dashboard correcto según el rol del usuario */
  const handleGoToDashboard = () => {
    if (user?.rol === "ADMIN") {
      navigate("/dashboard/admin");
    } else if (user?.rol === "CLIENTE") {
      navigate("/dashboard/cliente");
    } else {
      navigate("/login");
    }
  };

  return (
    <div className="error-container">
      <div className="error-content">
        <div className="error-icon">🚫</div>
        <h1 className="error-title">Acceso Denegado</h1>
        <p className="error-message">
          No tienes permisos para acceder a esta página.
        </p>
        <p className="error-description">
          Tu rol actual es: <strong>{user?.rol || "Sin rol"}</strong>
        </p>
        <div className="error-actions">
          <button 
            onClick={handleGoToDashboard}
            className="btn-primary"
          >
            Volver al Inicio
          </button>
        </div>
      </div>
    </div>
  );
};

export default Unauthorized;

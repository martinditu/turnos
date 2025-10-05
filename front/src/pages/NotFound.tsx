/*pattern return: Página de error 404 - Página No Encontrada */
/* Se muestra cuando el usuario intenta acceder a una ruta que no existe */

import { useNavigate } from "react-router-dom";

const NotFound = () => {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate("/");
  };

  return (
    <div className="error-container">
      <div className="error-content">
        <div className="error-icon">🔍</div>
        <h1 className="error-title">Página No Encontrada</h1>
        <p className="error-code">Error 404</p>
        <p className="error-message">
          La página que buscas no existe o fue movida.
        </p>
        <div className="error-actions">
          <button 
            onClick={handleGoHome}
            className="btn-primary"
          >
            Volver al Inicio
          </button>
        </div>
      </div>
    </div>
  );
};

export default NotFound;

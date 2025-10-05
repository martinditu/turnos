/*pattern return: Página de error 500 - Error del Servidor */
/* Se muestra cuando hay un error inesperado en el backend */
/* Proporciona información útil para el usuario y opciones de recuperación */

import { useNavigate, useLocation } from "react-router-dom";

const ServerError = () => {
  const navigate = useNavigate();
  const location = useLocation();
  
  /*pattern return: Recupera el mensaje de error personalizado si existe */
  const errorMessage = location.state?.message || "Ocurrió un error inesperado en el servidor.";

  const handleGoBack = () => {
    navigate(-1);
  };

  const handleGoHome = () => {
    navigate("/");
  };

  return (
    <div className="error-container">
      <div className="error-content">
        <div className="error-icon">⚠️</div>
        <h1 className="error-title">Error del Servidor</h1>
        <p className="error-code">Error 500</p>
        <p className="error-message">{errorMessage}</p>
        <p className="error-description">
          Por favor, intenta nuevamente más tarde o contacta al soporte si el problema persiste.
        </p>
        <div className="error-actions">
          <button 
            onClick={handleGoBack}
            className="btn-secondary"
          >
            Volver Atrás
          </button>
          <button 
            onClick={handleGoHome}
            className="btn-primary"
          >
            Ir al Inicio
          </button>
        </div>
      </div>
    </div>
  );
};

export default ServerError;

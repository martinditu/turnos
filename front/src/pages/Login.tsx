/*pattern return: Página de login mejorada con diseño moderno */
/* Incluye enlace al registro de nuevos usuarios */

import { useState, FormEvent } from "react";
import { useNavigate, Link, useLocation } from "react-router-dom";
import { loginUsuario } from "../api/authService";
import { useAuthContext } from "../context/AuthContext";
import useDocumentTitle from "../hooks/useDocumentTitle";

const Login = () => {
  useDocumentTitle("Login - Sistema de Turnos");

  const [formData, setFormData] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { login } = useAuthContext();
  const navigate = useNavigate();
  const location = useLocation();
  
  /*pattern return: Mensaje de éxito si viene desde registro */
  const successMessage = location.state?.message;

  const manejarCambio = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const redirigirPorRol = (rol: string) => {
    if (rol === "ADMIN") navigate("/dashboard/admin");
    else if (rol === "CLIENTE") navigate("/dashboard/cliente/menu");
    else navigate("/unauthorized");
  };

  const manejarEnvio = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await loginUsuario(formData.email, formData.password);
      login({
        email: response.email,
        token: response.token,
        rol: response.rol,
      });
      redirigirPorRol(response.rol);
    } catch (err: any) {
      setError("Credenciales incorrectas o error en la conexion.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <h1>Bienvenido</h1>
          <p>Inicia sesión en el sistema de turnos</p>
        </div>

        {/*pattern return: Mensaje de éxito al registrarse */}
        {successMessage && (
          <div className="success-message">{successMessage}</div>
        )}

        <form onSubmit={manejarEnvio} noValidate className="login-form">
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              placeholder="correo@ejemplo.com"
              value={formData.email}
              onChange={manejarCambio}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Contraseña</label>
            <input
              type="password"
              id="password"
              name="password"
              placeholder="Tu contraseña"
              value={formData.password}
              onChange={manejarCambio}
              required
            />
          </div>

          {error && <div className="error-alert">{error}</div>}

          <button
            type="submit"
            disabled={loading}
            className="btn-login"
          >
            {loading ? "Ingresando..." : "Iniciar Sesión"}
          </button>

          {/*pattern return: Enlace al registro de nuevos clientes */}
          <div className="register-link">
            ¿No tienes cuenta? <Link to="/register">Regístrate aquí</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;

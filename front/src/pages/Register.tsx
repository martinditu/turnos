/*pattern return: Página de registro de nuevos clientes */
/* Permite que cualquier persona se registre en el sistema como cliente */
/* Resuelve el problema: "No hay registro de clientes" */

import { useState, FormEvent } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registrarCliente } from "../api/authService";

const Register = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  
  /*pattern return: Estado del formulario */
  const [formData, setFormData] = useState({
    nombre: "",
    apellido: "",
    email: "",
    password: "",
    confirmPassword: "",
    telefono: ""
  });

  /*pattern return: Manejo del cambio en los inputs */
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
    setError(""); // Limpia el error al escribir
  };

  /*pattern return: Validación y envío del formulario */
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    // Validación de contraseñas
    if (formData.password !== formData.confirmPassword) {
      setError("Las contraseñas no coinciden");
      setLoading(false);
      return;
    }

    // Validación de longitud mínima
    if (formData.password.length < 6) {
      setError("La contraseña debe tener al menos 6 caracteres");
      setLoading(false);
      return;
    }

    try {
      /*pattern return: Envía los datos al backend */
      await registrarCliente({
        nombre: formData.nombre,
        apellido: formData.apellido,
        email: formData.email,
        password: formData.password,
        telefono: formData.telefono
      });

      setSuccess(true);
      
      /*pattern return: Redirige al login después de 2 segundos */
      setTimeout(() => {
        navigate("/login", { state: { message: "Registro exitoso. Por favor inicia sesión." } });
      }, 2000);

    } catch (err: any) {
      /*pattern return: Manejo de errores del servidor */
      if (err.response?.status === 500) {
        navigate("/server-error", { state: { message: err.response.data?.mensaje || "Error al registrar el cliente" } });
      } else {
        setError(err.response?.data?.mensaje || "Error al registrar. Intenta nuevamente.");
      }
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="register-container">
        <div className="register-card success-card">
          <div className="success-icon">✓</div>
          <h2>¡Registro Exitoso!</h2>
          <p>Tu cuenta ha sido creada correctamente.</p>
          <p>Redirigiendo al login...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="register-container">
      <div className="register-card">
        <div className="register-header">
          <h1>Crear Cuenta</h1>
          <p>Regístrate para comenzar a reservar turnos</p>
        </div>

        <form onSubmit={handleSubmit} className="register-form">
          {error && <div className="error-alert">{error}</div>}

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="nombre">Nombre *</label>
              <input
                type="text"
                id="nombre"
                name="nombre"
                value={formData.nombre}
                onChange={handleChange}
                required
                minLength={2}
                maxLength={50}
                placeholder="Tu nombre"
              />
            </div>

            <div className="form-group">
              <label htmlFor="apellido">Apellido *</label>
              <input
                type="text"
                id="apellido"
                name="apellido"
                value={formData.apellido}
                onChange={handleChange}
                required
                minLength={2}
                maxLength={50}
                placeholder="Tu apellido"
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="email">Email *</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="correo@ejemplo.com"
            />
          </div>

          <div className="form-group">
            <label htmlFor="telefono">Teléfono *</label>
            <input
              type="tel"
              id="telefono"
              name="telefono"
              value={formData.telefono}
              onChange={handleChange}
              required
              placeholder="1122334455"
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="password">Contraseña *</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
                minLength={6}
                placeholder="Mínimo 6 caracteres"
              />
            </div>

            <div className="form-group">
              <label htmlFor="confirmPassword">Confirmar Contraseña *</label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                required
                minLength={6}
                placeholder="Repetir contraseña"
              />
            </div>
          </div>

          <button 
            type="submit" 
            className="btn-register"
            disabled={loading}
          >
            {loading ? "Registrando..." : "Crear Cuenta"}
          </button>

          <div className="login-link">
            ¿Ya tienes cuenta? <Link to="/login">Inicia Sesión</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register;

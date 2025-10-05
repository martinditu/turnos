import { useState, FormEvent } from "react";
import { useNavigate } from "react-router-dom";
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
    <div className="max-w-sm mx-auto mt-20 p-6 border rounded shadow">
      <h2 className="text-xl font-bold mb-4 text-center">Iniciar sesion</h2>

      <form onSubmit={manejarEnvio} noValidate className="space-y-4">
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={manejarCambio}
          required
          className="w-full p-2 border rounded"
        />

        <input
          type="password"
          name="password"
          placeholder="Contrasena"
          value={formData.password}
          onChange={manejarCambio}
          required
          className="w-full p-2 border rounded"
        />

        {error && <p className="text-red-600 text-sm">{error}</p>}

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
        >
          {loading ? "Ingresando..." : "Entrar"}
        </button>
      </form>
    </div>
  );
};

export default Login;


import { useState } from "react";
import { editarCliente } from "../api/adminApi"; 

export default function useEditarCliente() {
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const actualizarCliente = async (id: number, data: any) => {
    setCargando(true);
    setError(null);

    try {
      const clienteActualizado = await editarCliente(id, data);
      return clienteActualizado;
    } catch (err: any) {
      setError(err.message || "Error al actualizar cliente");
      throw err;
    } finally {
      setCargando(false);
    }
  };

  return { actualizarCliente, cargando, error };
}

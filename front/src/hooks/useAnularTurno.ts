import { useState } from "react";
import { anularTurno } from "../api/clienteApi";

// hook para manejar la anulacion
export default function useAnularTurno() {
  const [mensaje, setMensaje] = useState(""); // msj exito
  const [error, setError] = useState(""); // msj error
  const [loading, setLoading] = useState(false); // estado de carga

  // func q realiza la anulacion usando el codigo
  const anular = async (codigo: string) => {
    setMensaje("");
    setError("");
    setLoading(true);

    try {
      await anularTurno(codigo); // llama al servicio
      setMensaje("Turno anulado con exito.");
    } catch (err: any) {
      setError(err.message || "Error al anular turno");
    } finally {
      setLoading(false); // siempre se desactiva el loading
    }
  };

  // devuelve estados y func para usar en el componente
  return { mensaje, error, loading, anular };
}

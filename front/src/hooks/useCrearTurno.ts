import { useState } from "react";
import { crearTurno as apiCrearTurno } from "../api/clienteApi";

interface CrearTurnoParams {
  idServicio: number;
  idUbicacion: number;
  fecha: string;
  hora: string;
}

export default function useCrearTurno() {
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const crearTurno = async (datos: CrearTurnoParams) => {
    setCargando(true);
    setError(null);

    try {
      const turno = await apiCrearTurno(datos);
      return turno;
    } catch (e: any) {
      setError(e.message || "No se pudo crear el turno");
      throw e;
    } finally {
      setCargando(false);
    }
  };

  return { crearTurno, cargando, error };
}

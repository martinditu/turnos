import { useState, useEffect } from "react";
import { traerHorariosDisponibles } from "../api/clienteApi";

export default function useHorariosDisponibles(servicioId: number, fecha: string) {
  const [horarios, setHorarios] = useState<string[]>([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!servicioId || !fecha) {
      setHorarios([]);
      setCargando(false);
      return;
    }

    setCargando(true);
    setError(null);

    const cargarHorarios = async () => {
      try {
        const data = await traerHorariosDisponibles(servicioId, fecha);
        setHorarios(data);
      } catch (err: any) {
        setError(err.message || "Error al cargar los horarios");
      } finally {
        setCargando(false);
      }
    };

    cargarHorarios();
  }, [servicioId, fecha]);

  return { horarios, cargando, error };
}

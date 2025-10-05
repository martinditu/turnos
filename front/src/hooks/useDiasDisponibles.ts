import { useState, useEffect } from "react";
import { traerDiasDisponibles } from "../api/clienteApi";

// hook para traer los dias disp de un servicio
export default function useDiasDisponibles(servicioId: number) {
  const [dias, setDias] = useState<string[]>([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    setCargando(true);
    setError(null);

    const loadDias = async () => {
      try {
        const data = await traerDiasDisponibles(servicioId);
        setDias(data);
      } catch (err: any) {
        setError(err.message || "Error al cargar los dias disponibles");
      } finally {
        setCargando(false);
      }
    };

    if (servicioId) {
      loadDias();
    } else {
      setDias([]);
      setCargando(false);
    }
  }, [servicioId]);

  return { dias, cargando, error };
}

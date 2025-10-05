import { useState, useEffect, useCallback } from "react";
import { DisponibilidadResponse, Horario } from "../types/Disponibilidades";

// hook q maneja fechas y horarios disp para un servicio
function useFechasYHorarios(servicioId: number) {
  const [fechasHabilitadas, setFechasHabilitadas] = useState<string[]>([]);
  const [horarios, setHorarios] = useState<string[]>([]);

  // referencia de hoy sin hora
  const hoy = new Date();
  hoy.setHours(0, 0, 0, 0);

  // convierte fecha a string con formato yyyy-mm-dd considera zona horaria local
  const toLocalDateString = (date: Date): string => {
    const offset = date.getTimezoneOffset();
    const localDate = new Date(date.getTime() - offset * 60 * 1000);
    return localDate.toISOString().split("T")[0];
  };

  // carga las fecha con disp para un mes determinado
  const cargarFechasHabilitadas = useCallback(
    async (date: Date) => {
      const year = date.getFullYear();
      const month = date.getMonth() + 1;

      const token = localStorage.getItem("token") || "";

      try {
        const response = await fetch(
          `http://localhost:8080/api/cliente/servicios/${servicioId}/fechas-habilitadas?year=${year}&month=${month}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );

        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

        const data: string[] = await response.json();
        setFechasHabilitadas(data);
      } catch (error) {
        console.error("Error al cargar fechas disponibles:", error);
        setFechasHabilitadas([]);
      }
    },
    [servicioId]
  );

  // carga los horarios disp para una fecha seleccionada
  const cargarHorarios = useCallback(
    async (fechaISO: string) => {
      const token = localStorage.getItem("token") || "";

      try {
        const response = await fetch(
          `http://localhost:8080/api/cliente/servicio/${servicioId}/disponibilidad/${fechaISO}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );

        if (!response.ok) throw new Error(`Error http!!  ${response.status}`);

        const data: DisponibilidadResponse = await response.json();

        if (data.activo) {
          const horariosFormateados = data.horarios.map(
            (h) => `${h.horaInicio} - ${h.horaFin}`
          );
          setHorarios(horariosFormateados);
        } else {
          alert("Este dia no tiene disponibilidad.");
          setHorarios([]);
        }
      } catch (error) {
        console.error("Error al obtener disponibilidad:", error);
        setHorarios([]);
      }
    },
    [servicioId]
  );

  
  const fechaHabilitada = (date: Date) => {
    const isoDate = toLocalDateString(date);
    return fechasHabilitadas.includes(isoDate) && date.getTime() >= hoy.getTime();
  };

  return {
    fechasHabilitadas,
    horarios,
    cargarFechasHabilitadas,
    cargarHorarios,
    fechaHabilitada,
    toLocalDateString,
  };
}

export default useFechasYHorarios;

import React, { useState, useEffect } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

import useDocumentTitle from "../hooks/useDocumentTitle";
import useFechasYHorarios from "../hooks/useFechasYHorarios";

// props esperados por el componente
interface CalendarioTurnosProps {
  servicioId: number; // id del servicio para filtrar fechas
  onSelectFecha: (fecha: string) => void; // func para notificar fecha seleccionada
  onVolver?: () => void; // /*pattern return: Callback para volver al paso anterior */
}

// componente q muestra un calendario con fechas y horarios disp
function CalendarioTurnos({
  servicioId,
  onSelectFecha,
  onVolver,
}: CalendarioTurnosProps) {
  useDocumentTitle("CalendarioTurnos");

  // fecha actual seleccionada por el usuario
  const [fechaSeleccionada, setFechaSeleccionada] = useState<Date | null>(null);

  // hook q maneja fechas hab y horarios disp
  const {
    horarios,
    cargarFechasHabilitadas,
    cargarHorarios,
    fechaHabilitada,
    toLocalDateString, // convierte fecha a string con formato yyyy-mm-dd
  } = useFechasYHorarios(servicioId);

  // 'hoy' para cargar inicialmente
  const hoy = new Date();
  hoy.setHours(0, 0, 0, 0);

  // se cargan las fecha hab del mes actual
  useEffect(() => {
    cargarFechasHabilitadas(hoy);
  }, [cargarFechasHabilitadas]);


  const alCambiarMes = (date: Date) => {
    cargarFechasHabilitadas(date);
  };

  const alSeleccionarFecha = (date: Date | null) => {
    if (!date) return;
    setFechaSeleccionada(date);

    const fechaISO = toLocalDateString(date);
    onSelectFecha(fechaISO);
    cargarHorarios(fechaISO);
  };

  return (
    <div>
      <h2>Selecciona una fecha</h2>
      {/*pattern return: Botón para volver a la selección de ubicaciones */}
      {onVolver && (
        <button
          onClick={onVolver}
          className="mb-4 bg-gray-500 text-white py-2 px-4 rounded hover:bg-gray-600 transition"
        >
          ← Volver a Ubicaciones
        </button>
      )}
      <DatePicker
        selected={fechaSeleccionada}
        onChange={alSeleccionarFecha}
        minDate={hoy}
        filterDate={fechaHabilitada}
        onMonthChange={alCambiarMes}
        dateFormat="yyyy-MM-dd"
        placeholderText="Elige un dia"
        inline
      />

      {horarios.length > 0 && (
        <div>
          <h3>Horarios disponibles:</h3>
          <ul>
            {horarios.map((h, i) => (
              <li key={i}>{h}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}

export default CalendarioTurnos;

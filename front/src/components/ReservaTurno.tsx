import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ServicioResponseDTO, UbicacionResponseDTO } from "../types/Turno";
import ServiciosList from "./ServiciosList";
import UbicacionesList from "./UbicacionesList";
import CalendarioTurnos from "./CalendarioTurnos";
import HorariosDisponiblesList from "./HorariosDisponiblesList";
import ConfirmarTurno from "./ConfirmarTurno";
import useDocumentTitle from "../hooks/useDocumentTitle";

const ReservaTurno = () => {
  useDocumentTitle("ReservaTurno");

  const [servicio, setServicio] = useState<ServicioResponseDTO | null>(null);
  const [ubicacion, setUbicacion] = useState<UbicacionResponseDTO | null>(null);
  const [fecha, setFecha] = useState<string | null>(null);
  const [hora, setHora] = useState<string | null>(null);

  const navigate = useNavigate();

  const resetReserva = () => {
    setServicio(null);
    setUbicacion(null);
    setFecha(null);
    setHora(null);
    navigate("/dashboard/cliente/menu");
  };

  /*pattern return: Flujo paso a paso con navegación hacia atrás en cada etapa */
  /* Resuelve el problema: "No hay navegación entre las vistas" */
  
  if (!servicio) {
    return (
      <ServiciosList 
        onSelectServicio={setServicio}
        onVolver={() => navigate("/dashboard/cliente/menu")} 
      />
    );
  }

  if (!ubicacion) {
    return (
      <UbicacionesList
        servicioId={servicio.id}
        onSelectUbicacion={setUbicacion}
        onVolver={() => setServicio(null)} // /*pattern return: Vuelve a la selección de servicio */
      />
    );
  }

  if (!fecha) {
    return (
      <CalendarioTurnos 
        servicioId={servicio.id} 
        onSelectFecha={setFecha}
        onVolver={() => setUbicacion(null)} // /*pattern return: Vuelve a la selección de ubicación */
      />
    );
  }

  if (!hora) {
    return (
      <HorariosDisponiblesList
        servicioId={servicio.id}
        fecha={fecha}
        onSeleccionarHora={setHora}
        onVolver={() => setFecha(null)} // /*pattern return: Vuelve a la selección de fecha */
      />
    );
  }

  return (
    <ConfirmarTurno
      servicio={servicio}
      ubicacion={ubicacion}
      fecha={fecha}
      hora={hora}
      onVolver={() => setHora(null)} // /*pattern return: Vuelve a la selección de horario */
      onConfirmar={resetReserva}
    />
  );
};

export default ReservaTurno;

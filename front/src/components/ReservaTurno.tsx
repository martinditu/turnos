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

  if (!servicio) {
    return <ServiciosList onSelectServicio={setServicio} />;
  }

  if (!ubicacion) {
    return (
      <UbicacionesList
        servicioId={servicio.id}
        onSelectUbicacion={setUbicacion}
      />
    );
  }

  if (!fecha) {
    return (
      <CalendarioTurnos servicioId={servicio.id} onSelectFecha={setFecha} />
    );
  }

  if (!hora) {
    return (
      <HorariosDisponiblesList
        servicioId={servicio.id}
        fecha={fecha}
        onSeleccionarHora={setHora}
      />
    );
  }

  return (
    <ConfirmarTurno
      servicio={servicio}
      ubicacion={ubicacion}
      fecha={fecha}
      hora={hora}
      onVolver={() => setHora(null)}
      onConfirmar={resetReserva}
    />
  );
};

export default ReservaTurno;

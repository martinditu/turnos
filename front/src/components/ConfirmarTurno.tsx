import { ServicioResponseDTO, UbicacionResponseDTO } from "../types/Turno";
import useCrearTurno from "../hooks/useCrearTurno";

interface Props {
  servicio: ServicioResponseDTO;
  ubicacion: UbicacionResponseDTO;
  fecha: string;
  hora: string;
  onVolver: () => void;
  onConfirmar: () => void;
}

export default function ConfirmarTurno({
  servicio,
  ubicacion,
  fecha,
  hora,
  onVolver,
  onConfirmar,
}: Props) {
  const { crearTurno, cargando, error } = useCrearTurno();

  const confirmar = async () => {
    try {
      const turno = await crearTurno({
        idServicio: servicio.id,
        idUbicacion: ubicacion.id,
        fecha,
        hora,
      });
      alert(`Turno reservado. Código de anulación: ${turno.codigoAnulacion}`);
      onConfirmar();
    } catch {
      alert(error || "Error al reservar el turno.");
    }
  };

  return (
    <div>
      <h3>Confirmar turno</h3>
      <p>
        <strong>Servicio:</strong> {servicio.nombre}
      </p>
      <p>
        <strong>Ubicación:</strong> {ubicacion.direccion}
      </p>
      <p>
        <strong>Fecha:</strong> {formatearFecha(fecha)}
      </p>
      <p>
        <strong>Hora:</strong> {hora}
      </p>

      <button onClick={confirmar} disabled={cargando}>
        {cargando ? "Confirmando..." : "Confirmar"}
      </button>
      <button
        onClick={onVolver}
        disabled={cargando}
        style={{ marginLeft: "1rem" }}
      >
        Volver
      </button>

      {error && <p style={{ color: "red", marginTop: "1rem" }}>{error}</p>}
    </div>
  );
}

function formatearFecha(fecha: string): string {
  // cambia "anio-mes-dia" a "dia/mes/anio"
  return fecha.split("-").reverse().join("/");
}

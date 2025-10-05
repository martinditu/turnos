import useDocumentTitle from "../hooks/useDocumentTitle";
import useHorariosDisponibles from "../hooks/useHorariosDisponibles";

interface Props {
  servicioId: number;
  fecha: string; // formato "yyyy-MM-dd"
  onSeleccionarHora: (hora: string) => void;
}

function ListaHorariosDisponibles({
  servicioId,
  fecha,
  onSeleccionarHora,
}: Props) {
  useDocumentTitle("Horarios disponibles");

  const { horarios, cargando, error } = useHorariosDisponibles(
    servicioId,
    fecha
  );

  const formatearFecha = (fechaISO: string): string => {
    const [anio, mes, dia] = fechaISO.split("-").map(Number);
    return new Date(anio, mes - 1, dia).toLocaleDateString("es-AR");
  };

  if (cargando) return <div>Cargando horarios...</div>;
  if (error) return <div style={{ color: "red" }}>{error}</div>;
  if (horarios.length === 0) return <div>No hay horarios disponibles.</div>;

  return (
    <div>
      <h3>Horarios disponibles para {formatearFecha(fecha)}</h3>
      <ul className="flex flex-wrap gap-2 mt-2">
        {horarios.map((hora) => (
          <li key={hora}>
            <button
              onClick={() => onSeleccionarHora(hora)}
              className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700"
            >
              {hora}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ListaHorariosDisponibles;

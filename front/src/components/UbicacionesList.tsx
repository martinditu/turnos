import { useEffect, useState } from "react";
import { UbicacionResponseDTO } from "../types/Turno";
import { traerUbicaciones } from "../api/clienteApi"; // renombrado
import useDocumentTitle from "../hooks/useDocumentTitle";

interface Props {
  servicioId: number;
  onSelectUbicacion: (ubicacion: UbicacionResponseDTO) => void;
  onVolver?: () => void; // /*pattern return: Callback para volver al paso anterior */
}

// Lista de ubicaciones disponibles para un servicio
const UbicacionesList = ({ servicioId, onSelectUbicacion, onVolver }: Props) => {
  useDocumentTitle("UbicacionesList");

  const [ubicaciones, setUbicaciones] = useState<UbicacionResponseDTO[]>([]);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    const cargar = async () => {
      try {
        const data = await traerUbicaciones(servicioId);
        setUbicaciones(data);
      } catch (err) {
        console.error("Error al traer ubicaciones", err);
      } finally {
        setCargando(false);
      }
    };
    cargar();
  }, [servicioId]);

  if (cargando) return <div>Cargando ubicaciones...</div>;

  return (
    <div className="p-4 text-center">
      <h3 className="text-xl font-semibold mb-4">Ubicaciones disponibles</h3>
      {/*pattern return: Botón para volver a la selección de servicios */}
      {onVolver && (
        <button
          onClick={onVolver}
          className="mb-4 bg-gray-500 text-white py-2 px-4 rounded hover:bg-gray-600 transition"
        >
          ← Volver a Servicios
        </button>
      )}
      <ul className="space-y-2">
        {ubicaciones.map((u) => (
          <li key={u.id}>
            <button
              onClick={() => onSelectUbicacion(u)}
              className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
            >
              {u.direccion}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default UbicacionesList;

// lista de todos los turnos para admin

import { useEffect, useState } from "react";
import { TurnoResponseDTO } from "../types/Turno";
import { traerTodosLosTurnos } from "../api/adminApi";
import TurnoAdminList from "../components/TurnoAdminList";
import useDocumentTitle from "../hooks/useDocumentTitle";

const TurnoAdminListWrapper = () => {
  useDocumentTitle("Turnos | Administracion");

  const [turnos, setTurnos] = useState<TurnoResponseDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    traerTodosLosTurnos()
      .then(setTurnos)
      .catch((err) => console.error("Error al cargar turnos", err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Cargando turnos...</p>;

  return <TurnoAdminList turnos={turnos} />;
};

export default TurnoAdminListWrapper;

// para sacar la logica dentro del /routes/admin y del component en si.

import { useEffect, useState } from "react";
import { ClienteAdminDTO, ClientesAdminResponse } from "../types/Cliente";
import { traerClientes, darDeBajaCliente, darDeAltaCliente, editarCliente } from "../api/adminApi";

export const useClientes = () => {

  const [clientes, setClientes] = useState<{
    activos: ClienteAdminDTO[];
    dadosDeBaja: ClienteAdminDTO[];
  }>({ activos: [], dadosDeBaja: [] });

  const [loadingClientes, setLoadingClientes] = useState(true);

  useEffect(() => {

    const loadingClientes = async () => {

      try {

        const res: ClientesAdminResponse = await traerClientes();
        setClientes({
          activos: res.clientesActivos,
          dadosDeBaja: res.clientesBajaLogica,
        });
      } catch (err) {
        console.error("Error al cargarclientes", err);
      } finally {
        setLoadingClientes(false);
      }

    };
    loadingClientes();

  }, []);

  const handleDarDeBaja = async (id: number) => {
    try {
      await darDeBajaCliente(id);
      const cliente = clientes.activos.find((c) => c.id === id);
      if (!cliente) return;
      setClientes((prev) => ({
        activos: prev.activos.filter((c) => c.id !== id),
        dadosDeBaja: [...prev.dadosDeBaja, { ...cliente, clienteActivo: false },

        ],
      }));
    } catch (error) {
      console.error("Error al dar de baja", error);
      alert("No se pudo darde baja el cliente");
    }
  };

  const handleDarDeAlta = async (id: number) => {
    try {
      await darDeAltaCliente(id);
      const cliente = clientes.dadosDeBaja.find((c) => c.id === id);
      if (!cliente) return;
      setClientes((prev) => ({
        activos: [...prev.activos, { ...cliente, clienteActivo: true }],
        dadosDeBaja: prev.dadosDeBaja.filter((c) => c.id !== id),
      }));
    } catch (error) {
      console.error("Error al dar de alta", error);
      alert("No se pudo dar de ata al cliente");
    }
  };

  const handleEditarCliente = async (clienteEditado: ClienteAdminDTO) => {
    try {
      const actualizado = await editarCliente(clienteEditado.id, clienteEditado);
      setClientes((prev) => ({
        activos: prev.activos.map((c) =>
          c.id === actualizado.id ? actualizado : c
        ),
        dadosDeBaja: prev.dadosDeBaja.map((c) =>
          c.id === actualizado.id ? actualizado : c
        ),
      }));
    } catch (error) {
      console.error("Error al editar cliente", error);
      alert("No se pudo editar el cliente");
    }
  };

  return {
    clientes,
    loadingClientes,
    handleDarDeBaja,
    handleDarDeAlta,
    handleEditarCliente,
  };
};
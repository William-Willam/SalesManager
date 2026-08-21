import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { apiClient } from '../api/client';
import type { PageResponse, UsuarioRequest, UsuarioResponse, UsuarioUpdateRequest} from '../api/types';

export function useUsuarios() {
  return useQuery({
    queryKey: ['usuarios'],
    queryFn: async () => {
      const resposta = await apiClient.get<PageResponse<UsuarioResponse>>('/usuarios?size=100');
      return resposta.data.content;
    },
  });
}

export function useCriarUsuario() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (dados: UsuarioRequest) => {
      const resposta = await apiClient.post<UsuarioResponse>('/usuarios', dados);
      return resposta.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['usuarios'] });
    },
  });
}

export function useExcluirUsuario() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: number) => {
      await apiClient.delete(`/usuarios/${id}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['usuarios'] });
    },
  });
}

export function useAtualizarUsuario() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ id, dados }: { id: number; dados: UsuarioUpdateRequest }) => {
      const resposta = await apiClient.put<UsuarioResponse>(`/usuarios/${id}`, dados);
      return resposta.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['usuarios'] });
    },
  });
}
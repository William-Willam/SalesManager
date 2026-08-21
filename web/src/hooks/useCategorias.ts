import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { apiClient } from '../api/client';
import type { CategoriaRequest, CategoriaResponse, PageResponse } from '../api/types';

export function useCategorias() {
  return useQuery({
    queryKey: ['categorias'],
    queryFn: async () => {
      const resposta = await apiClient.get<PageResponse<CategoriaResponse>>('/categorias?size=100');
      return resposta.data.content;
    },
  });
}

export function useCriarCategoria() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (dados: CategoriaRequest) => {
      const resposta = await apiClient.post<CategoriaResponse>('/categorias', dados);
      return resposta.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categorias'] });
    },
  });
}

export function useAtualizarCategoria() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ id, dados }: { id: number; dados: CategoriaRequest }) => {
      const resposta = await apiClient.put<CategoriaResponse>(`/categorias/${id}`, dados);
      return resposta.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categorias'] });
    },
  });
}
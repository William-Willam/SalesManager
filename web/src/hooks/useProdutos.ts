import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { apiClient } from '../api/client';
import type { PageResponse, ProdutoRequest, ProdutoResponse } from '../api/types';

export function useProdutos() {
  return useQuery({
    queryKey: ['produtos'],
    queryFn: async () => {
      const resposta = await apiClient.get<PageResponse<ProdutoResponse>>('/produtos?size=100');
      return resposta.data.content;
    },
  });
}

export function useCriarProduto() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (dados: ProdutoRequest) => {
      const resposta = await apiClient.post<ProdutoResponse>('/produtos', dados);
      return resposta.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['produtos'] });
    },
  });
}

export function useAtualizarProduto() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ id, dados }: { id: number; dados: ProdutoRequest }) => {
      const resposta = await apiClient.put<ProdutoResponse>(`/produtos/${id}`, dados);
      return resposta.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['produtos'] });
    },
  });
}

export function useExcluirProduto() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: number) => {
      await apiClient.delete(`/produtos/${id}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['produtos'] });
    },
  });
}

export function useEnviarImagemProduto() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ id, arquivo }: { id: number; arquivo: File }) => {
      const formData = new FormData();
      formData.append('arquivo', arquivo);

      const resposta = await apiClient.post<ProdutoResponse>(
        `/produtos/${id}/imagem`,
        formData
      );
      return resposta.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['produtos'] });
    },
  });
}
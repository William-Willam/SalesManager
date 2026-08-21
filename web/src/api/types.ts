export type Papel = 'ADMIN' | 'GERENTE' | 'ATENDENTE';

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  nome: string;
  email: string;
  papel: Papel;
}

export interface UsuarioRequest {
  nome: string;
  email: string;
  senha: string;
  papel: Papel;
}

export interface UsuarioResponse {
  id: number;
  nome: string;
  email: string;
  papel: Papel;
  ativo: boolean;
}


export interface UsuarioUpdateRequest {
  nome: string;
  email: string;
  senha: string;
  papel: Papel;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
}

export interface ErroResposta {
  timestamp: string;
  status: number;
  erro: string;
  mensagem: string;
  detalhes: string[];
}

export interface ProdutoMaisVendido {
  nome: string;
  quantidadeVendida: number;
}

export interface VendaPorDia {
  data: string;
  total: number;
}

export interface DashboardResponse {
  totalVendas: number;
  quantidadeVendas: number;
  vendasPorDia: VendaPorDia[];
  produtosMaisVendidos: ProdutoMaisVendido[];
}

export interface CategoriaResponse {
  id: number;
  nome: string;
}

export interface CategoriaRequest {
  nome: string;
}

export interface ProdutoResponse {
  id: number;
  nome: string;
  descricao: string;
  preco: number;
  categoria: CategoriaResponse;
  ativo: boolean;
  imagemUrl: string | null;
}

export interface ProdutoRequest {
  nome: string;
  descricao: string;
  preco: number;
  categoria: { id: number };
}


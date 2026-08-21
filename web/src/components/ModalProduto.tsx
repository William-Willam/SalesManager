import { useState, type FormEvent } from 'react';
import toast from 'react-hot-toast';
import { useCategorias, useCriarCategoria } from '../hooks/useCategorias';
import { useCriarProduto, useAtualizarProduto, useEnviarImagemProduto } from '../hooks/useProdutos';
import { ApiException, SERVER_URL } from '../api/client';
import type { ProdutoResponse } from '../api/types';

export function ModalProduto({
  produto,
  aoFechar,
}: {
  produto?: ProdutoResponse;
  aoFechar: () => void;
}) {
  const ehEdicao = !!produto;

  const { data: categorias } = useCategorias();
  const criarCategoria = useCriarCategoria();
  const criarProduto = useCriarProduto();
  const atualizarProduto = useAtualizarProduto();
  const enviarImagem = useEnviarImagemProduto();

  const [nome, setNome] = useState(produto?.nome ?? '');
  const [descricao, setDescricao] = useState(produto?.descricao ?? '');
  const [preco, setPreco] = useState(produto ? String(produto.preco) : '');
  const [categoriaId, setCategoriaId] = useState(produto ? String(produto.categoria.id) : '');
  const [novaCategoria, setNovaCategoria] = useState('');
  const [criandoCategoria, setCriandoCategoria] = useState(false);
  const [arquivo, setArquivo] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(
    produto?.imagemUrl ? `${SERVER_URL}${produto.imagemUrl}` : null
  );
  const [erro, setErro] = useState<string | null>(null);

  const salvando = criarProduto.isPending || atualizarProduto.isPending || enviarImagem.isPending;

  function aoEscolherArquivo(e: React.ChangeEvent<HTMLInputElement>) {
    const arquivoEscolhido = e.target.files?.[0] ?? null;
    setArquivo(arquivoEscolhido);
    if (arquivoEscolhido) {
      setPreview(URL.createObjectURL(arquivoEscolhido));
    }
  }

  function aoCriarCategoriaClicar() {
    if (!novaCategoria.trim()) return;

    criarCategoria.mutate(
      { nome: novaCategoria.trim() },
      {
        onSuccess: (categoria) => {
          setCategoriaId(String(categoria.id));
          setNovaCategoria('');
          setCriandoCategoria(false);
          toast.success('Categoria criada com sucesso!');
        },
        onError: () => setErro('Erro ao criar categoria'),
      }
    );
  }

  function enviarImagemSeHouver(idProduto: number) {
    if (arquivo) {
      enviarImagem.mutate(
        { id: idProduto, arquivo },
        {
          onSuccess: () => {
            toast.success(ehEdicao ? 'Produto atualizado com sucesso!' : 'Produto criado com sucesso!');
            aoFechar();
          },
          onError: () => setErro('Produto salvo, mas houve erro ao enviar a imagem'),
        }
      );
    } else {
      toast.success(ehEdicao ? 'Produto atualizado com sucesso!' : 'Produto criado com sucesso!');
      aoFechar();
    }
  }

  function aoSubmeter(evento: FormEvent) {
    evento.preventDefault();
    setErro(null);

    if (!categoriaId) {
      setErro('Selecione uma categoria');
      return;
    }

    const dados = {
      nome,
      descricao,
      preco: Number(preco),
      categoria: { id: Number(categoriaId) },
    };

    if (ehEdicao) {
      atualizarProduto.mutate(
        { id: produto.id, dados },
        {
          onSuccess: () => enviarImagemSeHouver(produto.id),
          onError: (erro: unknown) => {
            const mensagem = erro instanceof ApiException ? erro.message : 'Erro ao atualizar produto';
            setErro(mensagem);
          },
        }
      );
    } else {
      criarProduto.mutate(dados, {
        onSuccess: (produtoCriado) => enviarImagemSeHouver(produtoCriado.id),
        onError: (erro: unknown) => {
          const mensagem = erro instanceof ApiException ? erro.message : 'Erro ao criar produto';
          setErro(mensagem);
        },
      });
    }
  }

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4">
      <form
        onSubmit={aoSubmeter}
        className="bg-white rounded-lg p-8 w-full max-w-lg flex flex-col gap-5"
      >
        <h3 className="text-lg font-bold text-slate-800">
          {ehEdicao ? 'Editar Produto' : 'Novo Produto'}
        </h3>

        <div className="flex gap-5">
          <div className="flex flex-col items-center gap-2">
            <div className="w-24 h-24 rounded-lg bg-slate-100 overflow-hidden flex items-center justify-center border border-slate-200">
              {preview ? (
                <img src={preview} alt="Preview" className="w-full h-full object-cover" />
              ) : (
                <span className="text-slate-300 text-3xl">🍽</span>
              )}
            </div>
            <label className="text-xs text-orange-500 font-bold cursor-pointer">
              Escolher foto
              <input type="file" accept="image/*" onChange={aoEscolherArquivo} className="hidden" />
            </label>
          </div>

          <div className="flex-1 flex flex-col gap-3">
            <div className="flex flex-col gap-1">
              <label className="text-sm text-slate-600">Nome</label>
              <input
                value={nome}
                onChange={(e) => setNome(e.target.value)}
                className="border rounded px-3 py-2 outline-none"
                required
              />
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div className="flex flex-col gap-1">
                <label className="text-sm text-slate-600">Preço</label>
                <input
                  type="number"
                  step="0.01"
                  min="0.01"
                  value={preco}
                  onChange={(e) => setPreco(e.target.value)}
                  className="border rounded px-3 py-2 outline-none"
                  required
                />
              </div>

              <div className="flex flex-col gap-1">
                <label className="text-sm text-slate-600">Categoria</label>
                <select
                  value={categoriaId}
                  onChange={(e) => setCategoriaId(e.target.value)}
                  className="border rounded px-3 py-2 outline-none"
                >
                  <option value="">Selecione...</option>
                  {categorias?.map((c) => (
                    <option key={c.id} value={c.id}>
                      {c.nome}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </div>
        </div>

        <div className="flex flex-col gap-1">
          <label className="text-sm text-slate-600">Descrição</label>
          <input
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            className="border rounded px-3 py-2 outline-none"
          />
        </div>

        {!criandoCategoria ? (
          <button
            type="button"
            onClick={() => setCriandoCategoria(true)}
            className="text-orange-500 text-sm font-bold text-left"
          >
            + Criar nova categoria
          </button>
        ) : (
          <div className="flex gap-2">
            <input
              value={novaCategoria}
              onChange={(e) => setNovaCategoria(e.target.value)}
              placeholder="Nome da categoria"
              className="border rounded px-3 py-2 outline-none flex-1"
            />
            <button
              type="button"
              onClick={aoCriarCategoriaClicar}
              className="bg-slate-700 text-white text-sm font-bold px-3 rounded"
            >
              Criar
            </button>
            <button
              type="button"
              onClick={() => setCriandoCategoria(false)}
              className="text-slate-400 text-sm"
            >
              Cancelar
            </button>
          </div>
        )}

        {erro && <p className="text-red-500 text-sm">{erro}</p>}

        <div className="flex gap-3 mt-2">
          <button
            type="button"
            onClick={aoFechar}
            className="flex-1 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold py-2 rounded"
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={salvando}
            className="flex-1 bg-orange-500 hover:bg-orange-400 disabled:opacity-50 text-white font-bold py-2 rounded"
          >
            {salvando ? 'Salvando...' : 'Salvar'}
          </button>
        </div>
      </form>
    </div>
  );
}
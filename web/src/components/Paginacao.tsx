export function Paginacao({
  paginaAtual,
  totalPaginas,
  aoMudarPagina,
}: {
  paginaAtual: number;
  totalPaginas: number;
  aoMudarPagina: (pagina: number) => void;
}) {
  if (totalPaginas <= 1) return null;

  return (
    <div className="flex justify-center items-center gap-2 mt-4">
      <button
        onClick={() => aoMudarPagina(paginaAtual - 1)}
        disabled={paginaAtual === 1}
        className="px-3 py-1 rounded text-sm font-bold bg-white border border-slate-200 text-slate-600 disabled:opacity-40 hover:bg-slate-50"
      >
        Anterior
      </button>
      <span className="text-sm text-slate-500">
        Página {paginaAtual} de {totalPaginas}
      </span>
      <button
        onClick={() => aoMudarPagina(paginaAtual + 1)}
        disabled={paginaAtual === totalPaginas}
        className="px-3 py-1 rounded text-sm font-bold bg-white border border-slate-200 text-slate-600 disabled:opacity-40 hover:bg-slate-50"
      >
        Próxima
      </button>
    </div>
  );
}
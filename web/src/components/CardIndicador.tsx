export function CardIndicador({ titulo, valor }: { titulo: string; valor: string }) {
  return (
    <div className="bg-white rounded-lg p-6 shadow-sm">
      <p className="text-slate-500 text-sm font-medium">{titulo}</p>
      <p className="text-3xl font-bold text-slate-800 mt-2">{valor}</p>
    </div>
  );
}
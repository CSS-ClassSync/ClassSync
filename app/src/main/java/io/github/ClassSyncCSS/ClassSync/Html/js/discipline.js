async function loadCSV(url) {
  const response = await fetch(url);
  const text = await response.text();
  return Papa.parse(text, { header: true, skipEmptyLines: true }).data;
}

async function main() {
  const materii = (await loadCSV('data/materii.csv')).filter(m => m.id && m.name && m.tip);
  const profesoriMaterii = await loadCSV('data/profesori_materii.csv');
  const profesori = (await loadCSV('data/profesori.csv')).filter(p => p.id && p.name);
  const list = document.getElementById('listaDiscipline');

  materii.forEach(materie => {
    const profIds = profesoriMaterii
      .filter(pm => pm.materie === materie.id)
      .map(pm => pm.profesor);

    const nameProfesori = profIds
      .map(id => {
        const prof = profesori.find(p => p.id === id);
        return prof ? prof.name : null;
      })
      .filter(Boolean);

    if (!materie.name || !materie.tip) return;

    const li = document.createElement('li');
    const link = document.createElement('a');
    link.href = `orar_discipline.html?materie=${encodeURIComponent(materie.name)}`;
    link.textContent = `${materie.name} (${materie.tip}) - ${nameProfesori.length > 0 ? nameProfesori.join(', ') : '(fara profesori)'}`;
    li.appendChild(link);
    list.appendChild(li);
  });
}

window.main = main;

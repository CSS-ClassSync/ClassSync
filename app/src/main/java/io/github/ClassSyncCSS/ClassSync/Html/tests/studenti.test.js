describe('studenti.js', () => {
  beforeEach(() => {
    document.body.innerHTML = `
      <ul id="listaGrupe"></ul>
    `;

    global.Papa = {
      parse: jest.fn((text, options) => {
        if (text.includes('id,an')) {
          return {
            data: [
              { id: 'G1', an: 'A1' },
              { id: 'G2', an: 'A2' }
            ]
          };
        }

        if (text.includes('spec')) {
          return {
            data: [
              { id: 'A1', an: '1', spec: 'Informatica', type: 'Licenta' },
              { id: 'A2', an: '2', spec: 'Matematica', type: 'Licenta' }
            ]
          };
        }

        return { data: [] };
      })
    };

    global.fetch = jest.fn((url) => {
      if (url.includes('ani.csv')) {
        return Promise.resolve({
          text: () => Promise.resolve(`id,an,spec,type\nA1,1,Informatica,Licenta\nA2,2,Matematica,Licenta`)
        });
      }
      if (url.includes('grupe.csv')) {
        return Promise.resolve({
          text: () => Promise.resolve(`id,an\nG1,A1\nG2,A2`)
        });
      }
      return Promise.reject(new Error(`CSV necunoscut: ${url}`));
    });
  });

  it('window.main incarca CSV-urile si afiseaza corect lista grupe', async () => {
    require('../js/studenti.js');

    await window.main();
    await new Promise(resolve => setTimeout(resolve, 0));

    const lista = document.getElementById('listaGrupe');
    expect(lista).not.toBeNull();
    expect(lista.children.length).toBe(2);
    });

});

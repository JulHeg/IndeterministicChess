
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Threading;
using System.Windows.Forms;
using IndeterministicChess.Game;
using IndeterministicChess.Sound;

namespace IndeterministicChess
{
    static class Program
    {
        /// <summary>
        /// Der Haupteinstiegspunkt für die Anwendung.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            Thread game = new Thread(MainGame.entry);
            Thread sound = new Thread(MainSound.entry);
            Application.Run(new MainGraphics());
        }
    }
}

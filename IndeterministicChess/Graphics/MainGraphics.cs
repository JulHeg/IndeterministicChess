using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using SharpGL;

namespace IndeterministicChess
{
    public partial class MainGraphics : Form
    {
        private OpenGL glContext;

        public MainGraphics()
        {
            InitializeComponent();
        }

        private void render(object sender, RenderEventArgs args)
        {
            
        }

        private void init(object sender, EventArgs e)
        {
            
        }

        private void load_MainGraphics(object sender, EventArgs e)
        {
            glContext = openGLControl.OpenGL;
        }
    }
}

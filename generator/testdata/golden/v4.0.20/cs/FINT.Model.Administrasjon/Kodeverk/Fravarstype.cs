using System;
using System.Collections.Generic;



using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Administrasjon.Kodeverk
{
	public class Fravarstype : Begrep {
		public enum Relasjonsnavn
        {
			LONNSART
        }
        
	
		public bool? Overfores { get; set; }
		
	}
}

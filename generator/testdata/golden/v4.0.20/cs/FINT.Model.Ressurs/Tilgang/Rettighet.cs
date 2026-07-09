using System;
using System.Collections.Generic;



using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Ressurs.Tilgang
{
	public class Rettighet : Begrep {
		public enum Relasjonsnavn
        {
			IDENTITET
        }
        
	
		public string Beskrivelse { get; set; }
		
	}
}

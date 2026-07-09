using System;
using System.Collections.Generic;



namespace FINT.Model.Arkiv.Noark
{
	public class Dokumentobjekt {
		public enum Relasjonsnavn
        {
			FILFORMAT,
			VARIANTFORMAT,
			OPPRETTETAV,
			REFERANSEDOKUMENTFIL
        }
        
	
		public string Filstorrelse { get; set; }
		public string FormatDetaljer { get; set; }
		public string Sjekksum { get; set; }
		public string SjekksumAlgoritme { get; set; }
		public long? Versjonsnummer { get; set; }
		
	}
}

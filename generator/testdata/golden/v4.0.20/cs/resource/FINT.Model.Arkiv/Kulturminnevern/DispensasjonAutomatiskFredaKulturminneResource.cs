using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Arkiv.Noark;

namespace FINT.Model.Arkiv.Kulturminnevern
{

    public class DispensasjonAutomatiskFredaKulturminneResource : SaksmappeResource 
    {

    
        public string KulturminneId { get; set; }
        public MatrikkelnummerResource Matrikkelnummer { get; set; }
        public Identifikator Soknadsnummer { get; set; }
        public string Tiltak { get; set; }
        
    }
}

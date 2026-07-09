using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Arkiv.Noark;

namespace FINT.Model.Arkiv.Kulturminnevern
{

    public class TilskuddFartoyResource : SaksmappeResource 
    {

    
        public string FartoyNavn { get; set; }
        public string Kallesignal { get; set; }
        public string KulturminneId { get; set; }
        public Identifikator Soknadsnummer { get; set; }
        
    }
}

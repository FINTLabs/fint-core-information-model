using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{

    public class FravarsregistreringResource 
    {

    
        public bool ForesPaVitnemal { get; set; }
        public string Kommentar { get; set; }
        public Periode Periode { get; set; }
        public Identifikator SystemId { get; set; }
        
        public FravarsregistreringResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddRegistrertAv(Link link)
        {
            AddLink("registrertAv", link);
        }

        public void AddFaggruppe(Link link)
        {
            AddLink("faggruppe", link);
        }

        public void AddUndervisningsgruppe(Link link)
        {
            AddLink("undervisningsgruppe", link);
        }

        public void AddFravarstype(Link link)
        {
            AddLink("fravarstype", link);
        }

        public void AddElevfravar(Link link)
        {
            AddLink("elevfravar", link);
        }
    }
}

using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Personvern.Samtykke
{

    public class BehandlingResource 
    {

    
        public bool Aktiv { get; set; }
        public string Formal { get; set; }
        public DateTime? Slettet { get; set; }
        public Identifikator SystemId { get; set; }
        
        public BehandlingResource()
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
     
            

        public void AddBehandlingsgrunnlag(Link link)
        {
            AddLink("behandlingsgrunnlag", link);
        }

        public void AddPersonopplysning(Link link)
        {
            AddLink("personopplysning", link);
        }

        public void AddSamtykke(Link link)
        {
            AddLink("samtykke", link);
        }

        public void AddTjeneste(Link link)
        {
            AddLink("tjeneste", link);
        }
    }
}

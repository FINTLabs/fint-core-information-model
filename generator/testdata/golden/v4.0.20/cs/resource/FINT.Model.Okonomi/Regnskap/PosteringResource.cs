using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Administrasjon.Kompleksedatatyper;
using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Regnskap
{

    public class PosteringResource 
    {

    
        public long Belop { get; set; }
        public bool Debet { get; set; }
        public KontostrengResource Kontering { get; set; }
        public Identifikator PosteringsId { get; set; }
        
        public PosteringResource()
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
     
            

        public void AddTransaksjon(Link link)
        {
            AddLink("transaksjon", link);
        }
    }
}

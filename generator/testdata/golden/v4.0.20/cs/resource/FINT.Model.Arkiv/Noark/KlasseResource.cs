using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Arkiv.Noark
{

    public class KlasseResource 
    {

    
        public string KlasseId { get; set; }
        public int? Rekkefolge { get; set; }
        public SkjermingResource Skjerming { get; set; }
        public string Tittel { get; set; }
        
        public KlasseResource()
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
     
            

        public void AddKlassifikasjonssystem(Link link)
        {
            AddLink("klassifikasjonssystem", link);
        }
    }
}

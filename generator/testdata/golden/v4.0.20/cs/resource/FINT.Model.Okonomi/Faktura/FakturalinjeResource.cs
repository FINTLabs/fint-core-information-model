using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Okonomi.Faktura
{

    public class FakturalinjeResource 
    {

    
        public float Antall { get; set; }
        public List<string> Fritekst { get; set; }
        public long Pris { get; set; }
        
        public FakturalinjeResource()
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
     
            

        public void AddVare(Link link)
        {
            AddLink("vare", link);
        }
    }
}
